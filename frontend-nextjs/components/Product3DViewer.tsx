'use client';

import { Suspense, useRef, useState } from 'react';
import { Canvas, useFrame } from '@react-three/fiber';
import { OrbitControls, Environment, useGLTF, PerspectiveCamera, ContactShadows } from '@react-three/drei';
import { ProductDto } from '@/types/product';
import { motion } from 'framer-motion';
import * as THREE from 'three';

interface Product3DViewerProps {
  product: ProductDto;
}

function Model({ url }: { url: string }) {
  const { scene } = useGLTF(url);
  const meshRef = useRef<THREE.Group>(null);
  const [hovered, setHovered] = useState(false);

  useFrame((state) => {
    if (meshRef.current && !hovered) {
      meshRef.current.rotation.y += 0.005;
    }
  });

  return (
    <group
      ref={meshRef}
      onPointerOver={() => setHovered(true)}
      onPointerOut={() => setHovered(false)}
      scale={1}
    >
      <primitive object={scene} />
    </group>
  );
}

function LoadingSpinner() {
  return (
    <div className="absolute inset-0 flex items-center justify-center">
      <div className="w-12 h-12 border-4 border-[#E5E5E7] border-t-[#007AFF] rounded-full animate-spin" />
    </div>
  );
}

export default function Product3DViewer({ product }: Product3DViewerProps) {
  const [isLoading, setIsLoading] = useState(true);

  if (!product.model3dUrl) {
    return (
      <div className="rounded-[12px] border border-[#E5E5E7] bg-white shadow-sm p-16 text-center">
        <p className="text-body text-[#86868B]">
          Este producto no tiene modelo 3D disponible
        </p>
      </div>
    );
  }

  const modelUrl = `${process.env.NEXT_PUBLIC_API_URL}${product.model3dUrl}`;

  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      transition={{ duration: 0.5 }}
      className="rounded-[12px] border border-[#E5E5E7] bg-white shadow-sm aspect-square relative overflow-hidden"
    >
      {isLoading && <LoadingSpinner />}
      
      <Canvas
        camera={{ position: [0, 0, 5], fov: 50 }}
        gl={{ 
          antialias: true, 
          alpha: true,
          powerPreference: 'high-performance',
        }}
        onCreated={() => setIsLoading(false)}
      >
        <Suspense fallback={null}>
          {/* HDR Environment Map - Estilo Apple */}
          <Environment
            preset="studio"
            background={false}
          />

          {/* Luces PBR Físicas */}
          <ambientLight intensity={0.4} />
          <directionalLight 
            position={[5, 5, 5]} 
            intensity={1.2}
            castShadow
          />
          <directionalLight 
            position={[-5, 3, -5]} 
            intensity={0.6}
          />
          <pointLight position={[0, 10, 0]} intensity={0.5} />

          {/* Modelo 3D */}
          <Model url={modelUrl} />

          {/* Controles Suaves */}
          <OrbitControls
            enableZoom={true}
            enablePan={false}
            minDistance={2}
            maxDistance={8}
            enableDamping
            dampingFactor={0.05}
            autoRotate={false}
            autoRotateSpeed={0.5}
          />

          {/* Sombra de Contacto Suave */}
          <ContactShadows
            position={[0, -1, 0]}
            opacity={0.2}
            scale={10}
            blur={2}
            far={4}
          />

          {/* Cámara con perspectiva suave */}
          <PerspectiveCamera makeDefault position={[0, 0, 5]} />
        </Suspense>
      </Canvas>

      {/* Controles UI Flotantes */}
      <div className="absolute bottom-4 left-4 right-4 flex justify-center space-x-2">
        <div className="px-4 py-2 rounded-full bg-white/80 border border-[#E5E5E7] text-xs text-[#86868B]">
          Arrastra para rotar • Pellizca para zoom
        </div>
      </div>
    </motion.div>
  );
}
